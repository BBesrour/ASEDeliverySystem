import json
from threading import Timer
import time

try:
    import RPi.GPIO as GPIO
except ImportError:
    raise ImportError("RPi.GPIO not installed")
try:
    from mfrc522 import SimpleMFRC522
except ImportError:
    raise ImportError("mfrc522 not installed")

from ase_io.card_content import CardContent, InvalidCardContent


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
LIGHT_SENSOR_PIN = 19
GPIO.setup(LIGHT_SENSOR_PIN, GPIO.IN)
LIGHT_SENSOR_ONLY_UP_PIN = 26
GPIO.setup(LIGHT_SENSOR_ONLY_UP_PIN, GPIO.OUT, initial=GPIO.HIGH)


class ASEHardwareIn:
    def __init__(self):
        self.reader = SimpleMFRC522()
        self.token_listeners = []
        self._dark = False
        self.darkness_listeners = []

    def add_token_listener(self, listener):
        self.token_listeners.append(listener)

    def add_darkness_listener(self, listener):
        self.darkness_listeners.append(listener)

    def remove_darkness_listener(self, listener):
        self.darkness_listeners.remove(listener)

    def is_dark(self) -> bool:
        return GPIO.input(LIGHT_SENSOR_PIN) == GPIO.HIGH

    def _check_darkness_change(self):
        while True:
            if self.is_dark() != self._dark:
                print("Darkness changed to", self.is_dark())
                self._dark = self.is_dark()
                for listener in self.darkness_listeners:
                    listener(self._dark)
            time.sleep(0.1)

    def _read_token(self):
        while True:
            print("Trying to read...")
            _, text = self.reader.read()
            text = text.strip()
            print("Got text:", text)
            try:
                card_content = CardContent(text)
            except json.JSONDecodeError as e:
                print(e)
                card_content = InvalidCardContent()
            except KeyError as e:
                print("KeyError", e)
                card_content = InvalidCardContent()
            for listener in self.token_listeners:
                listener(card_content)

    def mainloop(self):
        t = Timer(0.1, self._check_darkness_change)
        t.start()
        self._read_token()
        t.join()
