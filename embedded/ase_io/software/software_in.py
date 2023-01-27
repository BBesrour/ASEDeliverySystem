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
    card_id = "abcdef123"
    card_token = textinput("Card Token", "Enter the card token:")
    if card_token is None:
        return InvalidCardContent()
    return CardContent(card_id, card_token)


class ASESoftwareIn:
    def __init__(self):
        self.token_listeners = []
        self.darkness_listeners = []
        self._dark = True
        self._dark_turtle = Turtle()
        self._set_up_dark_turtle()
        onscreenclick(self._on_click)
        onkeypress(self._toggle_dark, "d")

    def _set_up_dark_turtle(self):
        self._dark_turtle.shape("square")
        self._dark_turtle.penup()
        self._dark_turtle.color("white")
        self._update_dark_turtle()

    def add_token_listener(self, listener):
        self.token_listeners.append(listener)

    def add_darkness_listener(self, listener):
        self.darkness_listeners.append(listener)

    def remove_darkness_listener(self, listener):
        self.darkness_listeners.remove(listener)

    def _on_click(self, x, y):
        if self._dark_turtle.distance(x, y) < 10:
            self._toggle_dark()
        else:
            card_content = _card_content_input()
            for listener in self.token_listeners:
                listener(card_content)

    def _update_dark_turtle(self):
        if self._dark:
            self._dark_turtle.fillcolor("black")
        else:
            self._dark_turtle.fillcolor("white")

    def _toggle_dark(self):
        self._dark = not self._dark
        self._update_dark_turtle()
        for listener in self.darkness_listeners:
            listener(self._dark)

    def is_dark(self) -> bool:
        return self._dark
