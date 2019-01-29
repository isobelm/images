# images

This is a program for image processing.
There are two main options at present, 'Blocking' and 'Edge Detection'.

Edge Detection:
//TODO

Blocking:
'Blocking' involves recursively running throung an image and redrawing it with 'blocks'.
'Blocks' are rectangles of one colour.
If the disparity of colour between two sides of a block is above the 'Threshold', the block is split into two smaller blocks.
Eg. if the a block is predominantly white at the top and black at the bottom, it will be split in half along the horizontal axis.
The 'Threshold' value can be specified using the Threshold slider.
The image can only be 'Block'ed after a source image has been chosen, and the blocked image can only be saved after it's been blocked.
