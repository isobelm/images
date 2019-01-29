# images

This is a program for image processing.
There are two main options at present, 'Blocking' and 'Edge Detection'.

	Edge Detection:
The edge detection portion of the program can do several things;
The edge detection portion of the program has the most options.
Generally, the buttons should be pressed in order.
The 'Process image' button runs through an image and outputs an image with the edges detected.  This button must be pressed before any of the subsequent buttons can be.
Process is affected by the 'Edge Size' and 'Strength' sliders.
Edge Size determines the distance between pixels being considered for an edge.
Strength determines how much the edges are enhanced.
At any point after the Process Image button has been pressed, the output can be saved.
The 'Invert' button inverts the colours in the image.
The 'Threshold' button colours the pixels above the 'threshold' white, and those below it black.
The 'threshold' is determined by the position of the threshold sliders.

	Blocking:
'Blocking' involves recursively running throung an image and redrawing it with 'blocks'.
'Blocks' are rectangles of one colour.
If the disparity of colour between two sides of a block is above the 'Threshold', the block is split into two smaller blocks.
Eg. if the a block is predominantly white at the top and black at the bottom, it will be split in half along the horizontal axis.
The 'Threshold' value can be specified using the Threshold slider.
The image can only be 'Block'ed after a source image has been chosen, and the blocked image can only be saved after it's been blocked.
