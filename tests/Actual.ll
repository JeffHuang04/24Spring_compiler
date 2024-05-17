; ModuleID = 'module'
source_filename = "module"

@a = global i32 10

define i32 @main() {
mainEntry:
  %pointer = load i32, i32* @a, align 4
  %minus = sub i32 %pointer, 3
  store i32 %minus, i32* @a, align 4
  %pointer1 = alloca i32, align 4
  %pointer2 = load i32, i32* @a, align 4
  %add = add i32 %pointer2, 1
  store i32 %add, i32* %pointer1, align 4
  %pointer3 = load i32, i32* %pointer1, align 4
  %pointer4 = load i32, i32* @a, align 4
  %mul = mul i32 %pointer4, 2
  %add5 = add i32 %pointer3, %mul
  ret i32 %add5
}
