; ModuleID = 'module'
source_filename = "module"

@a = global i32 10

define i32 @main() {
mainEntry:
  %a = load i32, i32* @a, align 4
  %NEQ = icmp ne i32 %a, 10
  %NEQ1 = zext i1 %NEQ to i32
  %cond = icmp ne i32 %NEQ1, 0
  br i1 %cond, label %if_true, label %if_false

if_true:                                          ; preds = %mainEntry
  store i32 2, i32* @a, align 4
  br label %next

if_false:                                         ; preds = %mainEntry
  store i32 20, i32* @a, align 4
  br label %next

next:                                             ; preds = %if_false, %if_true
  %a2 = load i32, i32* @a, align 4
  ret i32 %a2
}
