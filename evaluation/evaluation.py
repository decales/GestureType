import tkinter as tk
import time
import text
import instructions

index = 0
currentInstruction = instructions.ins1
startTime = None


def onEdit(event):
    global index, startTime
    currentText = textBox.get("1.0", "end-1c").strip()
    if currentText == text.text[index]:
        index += 1
        elapsedTime = time.time() - startTime
        print("Trial {} complete in {:.2f} seconds".format(index + 1, elapsedTime))
        prompt.config(text=instructions.instructions[index])
        startTime = time.time()


def onStart():
    global startTime
    startButton.pack_forget()
    textBox.pack(padx=20, pady=20)
    prompt.pack()
    startTime = time.time()


root = tk.Tk()
root.geometry("800x250")
root.title("GestureType Evaluation")

textBox = tk.Text(root, height=4, width=50, wrap=tk.WORD)
textBox.insert("1.0", text.base)
textBox.bind("<KeyRelease>", onEdit)

prompt = tk.Label(root, text=currentInstruction)

startButton = tk.Button(root, text="Start", command=onStart)
startButton.pack(pady=20)

root.mainloop()
