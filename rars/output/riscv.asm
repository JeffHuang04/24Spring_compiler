# Simple RISC-V program with actual load/store instructions
.data
x: .word 10  # 定义一个全局变量 x，初始化为 10

.text
.globl _start

_start:
    li t0, 0           # 将 0 加载到寄存器 t0
    la t1, x           # 加载变量 x 的地址到 t1
    lw t2, 0(t1)       # 从地址 t1 加载 x 的值到 t2
    add t2, t2, t0     # t2 = t2 + t0
    sw t2, 0(t1)       # 将 t2 的值存回地址 t1
    li a7, 10          # 加载立即数 10 到 a7（exit 系统调用）
    ecall              # 执行系统调用退出程序

