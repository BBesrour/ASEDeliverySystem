import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
mfrc = SimpleMFRC522()

# just for testing ourselves

for i in range(1, 100):
    try:
        mfrc.write("x" * i)
        # verify
        text = mfrc.read()
        print("Text", text)
        if text != "x" * i:
            print("Failed to write length", i)
            break
    except KeyboardInterrupt:
        GPIO.cleanup()
        raise
print("Success!")
