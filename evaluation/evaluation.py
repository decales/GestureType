import tkinter as tk
import time
import instructions

index = 0
startTime = None


def onEdit(event=None):
    global index, startTime
    currentText = textBox.get("1.0", "end-1c").strip()
    if currentText == instructions.text[index]:
        elapsedTime = time.time() - startTime
        # Prints instruction index, instruction type, # cursor places, completion time
        print("{},{},{},{:.2f}".format(
            index, instructions.instructions[index][1], instructions.instructions[index][2], elapsedTime
        ))
        index += 1
        prompt.config(text=instructions.instructions[index][0])
        startTime = time.time()


def onStart():
    global startTime
    startButton.pack_forget()
    textBox.pack(padx=20, pady=20)
    prompt.pack()
    startTime = time.time()
    textBox.focus_set()
    textBox.mark_set("insert", "1.0")


def toggleContextMenu(event):
    contextMenu.tk_popup(event.x_root, event.y_root)


root = tk.Tk()
root.geometry("800x250")
root.title("GestureType Evaluation")

contextMenu = tk.Menu(root, tearoff=0)
contextMenu.add_command(label="Copy", command=lambda : [textBox.event_generate('<<Copy>>'), onEdit()])
contextMenu.add_command(label="Cut", command=lambda: [textBox.event_generate('<<Cut>>'), onEdit()])
contextMenu.add_command(label="Paste", command=lambda: [textBox.event_generate('<<Paste>>'), onEdit()])

textBox = tk.Text(root, height=4, width=50, wrap=tk.WORD)
textBox.insert("1.0", instructions.base)
textBox.bind("<KeyRelease>", onEdit)
textBox.bind("<Button-3>", toggleContextMenu)

prompt = tk.Label(root, text=instructions.ins1[0])

startButton = tk.Button(root, text="Start", command=onStart)
startButton.pack(pady=20)

root.mainloop()
