import json

from ase_io.card_content import CardContent

try:
    from mfrc522 import SimpleMFRC522
except ImportError:
    raise ImportError("mfrc522 not installed")


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

    def mainloop(self):
        while True:
            _, text = self.reader.read()
            card_content = card_content_from_json(text)
            for listener in self.listeners:
                listener(card_content)
