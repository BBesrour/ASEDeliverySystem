# one pin on high, one pin on low, one input pin

import RPi.GPIO as GPIO
import time

HIGH_PIN = 16
LOW_PIN = 26
INPUT_PIN = 6
LED_PIN = 17

# set up LEDs
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(HIGH_PIN, GPIO.OUT, initial=GPIO.HIGH)
GPIO.setup(LOW_PIN, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(INPUT_PIN, GPIO.IN)
GPIO.setup(LED_PIN, GPIO.OUT, initial=GPIO.LOW)

try:
    while True:
        time.sleep(.1)
        print("Input:", GPIO.input(INPUT_PIN))
        GPIO.output(LED_PIN, GPIO.input(INPUT_PIN))

except KeyboardInterrupt:
    GPIO.cleanup()
    print("Cleaned up.")