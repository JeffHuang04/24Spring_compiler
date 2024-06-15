; ModuleID = 'module'
source_filename = "module"

@a = global i32 1
@b = global i32 0

define i32 @main() {
mainEntry:
  %c = alloca i32, align 4
  store i32 3, i32* %c, align 4
  %c1 = load i32, i32* %c, align 4
  %a = load i32, i32* @a, align 4
  %add = add i32 %c1, %a
  %add2 = add i32 %add, 1
  store i32 %add2, i32* @b, align 4
  %d = alloca i32, align 4
  store i32 10, i32* %d, align 4
  %a3 = load i32, i32* @a, align 4
  %b = load i32, i32* @b, align 4
  %add4 = add i32 %a3, %b
  %c5 = load i32, i32* %c, align 4
  %add6 = add i32 %add4, %c5
  %d7 = load i32, i32* %d, align 4
  %add8 = add i32 %add6, %d7
  ret i32 %add8
}
