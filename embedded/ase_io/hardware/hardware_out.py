try:
    import RPi.GPIO as GPIO
except:
    raise ImportError("RPi.GPIO not installed")
import threading

LED_ON_TIME_SECONDS = 3

# set up LEDs
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GREEN_PIN = 17
RED_PIN = 15
GPIO.setup(GREEN_PIN, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(RED_PIN, GPIO.OUT, initial=GPIO.LOW)


class ASEHardwareOut:
    def __init__(self):
        self.color = None

    def update_led(self):
        if self.color:
            print("LED color: {}".format(self.color))
        else:
            print("LED off")
        if self.color == "red":
            GPIO.output(RED_PIN, GPIO.HIGH)
        elif self.color == "green":
            GPIO.output(GREEN_PIN, GPIO.HIGH)
        else:
            pass
            # GPIO.output(RED_PIN, GPIO.LOW)
            # GPIO.output(GREEN_PIN, GPIO.LOW)

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
