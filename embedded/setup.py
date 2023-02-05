import json
import os

print("This tool is helping you set up your config.json file.")

my_dir = os.path.dirname(os.path.realpath(__file__))
with open(os.path.join(my_dir, 'config.example.json')) as f:
    example_config = json.load(f)

if os.path.exists(os.path.join(my_dir, 'config.json')):
    override = input("config.json already exists! Override? [y/N]: ").lower() == 'y'

    if not override:
        exit(0)

config = example_config.copy()
for key in example_config:
    val = input(f"Enter a value for {key} [{example_config[key]}]: ")
    if val:
        config[key] = val

with open(os.path.join(my_dir, 'config.json'), 'w') as f:
    json.dump(config, f)

print("Wrote config.json!")