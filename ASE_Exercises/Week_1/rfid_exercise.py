import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
import time
reader = SimpleMFRC522()

try:
    while True:
        id, text = reader.read()
        print(f"Read: {id} with text: {text}")
        if input("Override content? (y/n): ") == "y":
            reader.write(input("Change to: "))
            time.sleep(5)
except KeyboardInterrupt:
    # GPIO must be cleaned up once you e x i t the s c r i p t
    # Otherwise , other s c r i p t s may not work as you expect
    GPIO.cleanup()
    raise