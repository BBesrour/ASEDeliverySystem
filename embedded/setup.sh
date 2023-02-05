#!/usr/bin/env bash

if [ ! -d "venv" ]; then
    python -m venv venv
fi
source venv/bin/activate
pip install -r requirements.txt
python setup.py

echo "Setup complete. Run \"python main.py\" to start the embedded server."
echo "To start it in the future, first run \"source venv/bin/activate\" and then \"python main.py\""