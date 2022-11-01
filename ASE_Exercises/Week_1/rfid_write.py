import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
writer = SimpleMFRC522()

# just for testing ourselves

try:
    writer.write(input("What to write? "))
except KeyboardInterrupt :
    # GPIO must be cleaned up once you e x i t the s c r i p t
    # Otherwise , other s c r i p t s may not work as you expect
    GPIO.cleanup()
    raise