import json

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
LIGHT_SENSOR_PIN = 24
GPIO.setup(LIGHT_SENSOR_PIN, GPIO.IN)


def card_content_from_json(json_str: str) -> CardContent:
    """Return a CardContent object from a JSON string."""
    json_dict = json.loads(json_str)
    return CardContent(json_dict["id"], json_dict["card_token"])


class ASEHardwareIn:
    def __init__(self):
        self.reader = SimpleMFRC522()
        self.listeners = []

    def add_listener(self, listener):
        self.listeners.append(listener)

    def is_dark(self) -> bool:
        print("Light sensor: {}".format(GPIO.input(LIGHT_SENSOR_PIN)))
        return GPIO.input(LIGHT_SENSOR_PIN) == GPIO.HIGH

    def mainloop(self):
        while True:
            _, text = self.reader.read()
            try:
                card_content = card_content_from_json(text)
            except json.JSONDecodeError:
                card_content = InvalidCardContent()
            except KeyError as e:
                print("KeyError", e)
                card_content = InvalidCardContent()
            for listener in self.listeners:
                listener(card_content)
