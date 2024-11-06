  .data
a:
  .word 1

  .data
b:
  .word 0

  .text
  .globl main
main:
  addi sp, sp, 0
mainEntry:
  li t0, 3
  mv s0, t0
  mv s1, s0
  la t0, a
  lw t0, 0(t0)
  mv s2, t0
  mv t0, s1
  mv t1, s2
  add t0, t0, t1
  mv s3, t0
  mv t0, s3
  li t1, 1
  add t0, t0, t1
  mv s4, t0
  la t1, b
  sw s4, 0(t1)
  li t0, 10
  mv s5, t0
  la t0, a
  lw t0, 0(t0)
  mv s6, t0
  la t0, b
  lw t0, 0(t0)
  mv s7, t0
  mv t0, s6
  mv t1, s7
  add t0, t0, t1
  mv s8, t0
  mv s9, s0
  mv t0, s8
  mv t1, s9
  add t0, t0, t1
  mv s10, t0
  mv s11, s5
  mv t0, s10
  mv t1, s11
  add t0, t0, t1
  mv a0, t0
  mv a0, a0
  addi sp, sp, 0
  li a7, 93
  ecall