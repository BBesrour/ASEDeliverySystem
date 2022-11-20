from turtle import *

from ase_io.card_content import CardContent, InvalidCardContent


# Usage:
# There will be a small window popping up.
# The background color will be green if the LED is green and red if the LED is red (black = off).
# You can enter the card ID and card token by clicking on the window.
# In the middle there will be a square indicating the state of the light sensor (white = light, black = dark).
# You can change the light sensor state by clicking on the square or by pressing "d".


def _card_content_input() -> CardContent:
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
        self._dark = False
        self._dark_turtle = Turtle()
        self._set_up_dark_turtle()
        onscreenclick(self._on_click)
        onkeypress(self._toggle_dark, "d")

    def _set_up_dark_turtle(self):
        self._dark_turtle.shape("square")
        self._dark_turtle.penup()
        self._dark_turtle.color("white")

    def add_listener(self, listener):
        self.listeners.append(listener)

    def _on_click(self, x, y):
        if self._dark_turtle.distance(x, y) < 10:
            self._toggle_dark()
        else:
            card_content = _card_content_input()
            for listener in self.listeners:
                listener(card_content)

    def _toggle_dark(self):
        self._dark = not self._dark
        if self._dark:
            self._dark_turtle.fillcolor("black")
        else:
            self._dark_turtle.fillcolor("white")

    def is_dark(self) -> bool:
        return self._dark
