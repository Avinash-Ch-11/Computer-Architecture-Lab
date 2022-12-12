	.data
a:
	10
	.text
main:
    load %x0, $a, %x4
    divi %x4, 2, %x6
    muli %x6, 2, %x5
    beq %x4, %x5, success
    addi %x0, 1, %x10
    end
success:
    subi %x10, 1, %x10
    end
