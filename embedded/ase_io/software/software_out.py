from turtle import *
import threading

LED_ON_TIME_SECONDS = 3


class ASESoftwareOut:
    def __init__(self):
        self.color = None

    def update_led(self):
        bgcolor(self.color or "black")

    def turn_off(self):
        self.color = None
        self.update_led()

    def light_red(self):
        self.color = "red"
        self.update_led()
        threading.Timer(LED_ON_TIME_SECONDS, self.turn_off).start()

    def light_green(self):
        self.color = "green"
        self.update_led()
        threading.Timer(LED_ON_TIME_SECONDS, self.turn_off).start()
