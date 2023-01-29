import json
from threading import Timer

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
LIGHT_SENSOR_PIN = 20
GPIO.setup(LIGHT_SENSOR_PIN, GPIO.IN)
LIGHT_SENSOR_ONLY_UP_PIN = 21
GPIO.setup(LIGHT_SENSOR_ONLY_UP_PIN, GPIO.OUT, initial=GPIO.HIGH)


def card_content_from_json(json_str: str) -> CardContent:
    """Return a CardContent object from a JSON string."""
    json_dict = json.loads(json_str)
    return CardContent(json_dict["id"], json_dict["card_token"])


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
        if self.is_dark() != self._dark:
            print("Darkness changed to", self.is_dark())
            self._dark = not self._dark
            for listener in self.darkness_listeners:
                listener(self._dark)
        Timer(0.1, self._check_darkness_change).start()

    def mainloop(self):
        self._check_darkness_change()
        while True:
            _, text = self.reader.read()
            print("Got text:", text)
            try:
                card_content = card_content_from_json(text)
            except json.JSONDecodeError as e:
                print(e)
                card_content = InvalidCardContent()
            except KeyError as e:
                print("KeyError", e)
                card_content = InvalidCardContent()
            for listener in self.token_listeners:
                listener(card_content)
