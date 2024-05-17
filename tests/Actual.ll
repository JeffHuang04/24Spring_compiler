; ModuleID = 'module'
source_filename = "module"

@g_var = global i32 2

define i32 @main() {
mainEntry:
  %pointer = alloca i32, align 4
  store i32 1, i32* %pointer, align 4
  %pointer1 = load i32, i32* %pointer, align 4
  %pointer2 = load i32, i32* @g_var, align 4
  %add = add i32 %pointer1, %pointer2
  ret i32 %add
}
