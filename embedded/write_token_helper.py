import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
writer = SimpleMFRC522()

import json

# just for testing ourselves

try:
    writer.write(json.dumps({
        "id": input("Card ID (C12345): ") or "C12345",
        "card_token": input("Card token (D1234567890): ") or "D1234567890",
    }))
except KeyboardInterrupt :
    GPIO.cleanup()
    raise
print("Success!")
