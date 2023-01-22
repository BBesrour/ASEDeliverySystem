import asyncio

try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop
from threading import Timer


async def on_off():
    while True:
        print("Green!")
        ase_out.light_green()
        await asyncio.sleep(1)
        ase_out.turn_off()
        print("Red!")
        ase_out.light_red()
        await asyncio.sleep(1)
        ase_out.turn_off()

if __name__ == '__main__':
    try:
        thread = Timer(1, lambda: asyncio.run(on_off()))
        thread.start()
        ase_mainloop()
    except KeyboardInterrupt:
        ase_out.turn_off()
