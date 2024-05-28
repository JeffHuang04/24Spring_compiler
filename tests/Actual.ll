; ModuleID = 'module'
source_filename = "module"

@a = external global i32
@b = global i32 1

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 1, i32* %a, align 4
  %c = alloca i32, align 4
  %b = load i32, i32* @b, align 4
  store i32 %b, i32* %c, align 4
  ret i32 0
}
