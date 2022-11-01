import RPi.GPIO as GPIO

# set up LEDs
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GREEN_PIN = 17
RED_PIN = 15
GPIO.setup(GREEN_PIN, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(RED_PIN, GPIO.OUT, initial=GPIO.LOW)

from mfrc522 import SimpleMFRC522
import time
reader = SimpleMFRC522()

import json

with open("credentials.json", "r") as f:
	users = json.load(f)

def authenticate(user_id):
	for user in users:
		if user["id"] == user_id: return True
	return False

def light_up(color):
	pin = RED_PIN
	if color == "green":
		pin = GREEN_PIN
	GPIO.output(pin, GPIO.HIGH)
	time.sleep(3)
	GPIO.output(pin, GPIO.LOW)

try:
    while True:
        id, text = reader.read()
        print(text)
        found_id = None
        try:
            found_id = json.loads(text)["id"]
        except:
            pass
        if authenticate(found_id):
            light_up("green")
        else:
            light_up("red")
except KeyboardInterrupt:
    # GPIO must be cleaned up once you e x i t the s c r i p t
    # Otherwise , other s c r i p t s may not work as you expect
    GPIO.cleanup()
    raise
