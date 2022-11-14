from turtle import *

from ase_io.card_content import CardContent, InvalidCardContent


def card_content_input() -> CardContent:
    """Return a CardContent object with the user input."""
    card_id = textinput("Card ID", "Enter the card ID:")
    if card_id is None:
        return InvalidCardContent()
    card_token = textinput("Card Token", "Enter the card token:")
    if card_token is None:
        return InvalidCardContent()
    return CardContent(card_id, card_token)


class ASESoftwareIn:
    def __init__(self):
        self.listeners = []
        onscreenclick(self.on_key)

    def add_listener(self, listener):
        self.listeners.append(listener)

    def on_key(self, x, y):
        card_content = card_content_input()
        for listener in self.listeners:
            listener(card_content)
