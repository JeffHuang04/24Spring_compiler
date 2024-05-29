; ModuleID = 'module'
source_filename = "module"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 0, i32* %a, align 4
  %count = alloca i32, align 4
  store i32 0, i32* %count, align 4
  br label %while_cond

while_cond:                                       ; preds = %next, %mainEntry
  %a1 = load i32, i32* %a, align 4
  %LE = icmp sle i32 %a1, 0
  %LE2 = zext i1 %LE to i32
  %cond = icmp ne i32 %LE2, 0
  br i1 %cond, label %while_body, label %while_end

while_body:                                       ; preds = %while_cond
  %a3 = load i32, i32* %a, align 4
  %minus = sub i32 %a3, 1
  store i32 %minus, i32* %a, align 4
  %count4 = load i32, i32* %count, align 4
  %add = add i32 %count4, 1
  store i32 %add, i32* %count, align 4
  %a5 = load i32, i32* %a, align 4
  %LT = icmp slt i32 %a5, -20
  %LT6 = zext i1 %LT to i32
  %cond7 = icmp ne i32 %LT6, 0
  br i1 %cond7, label %if_true, label %if_false

while_end:                                        ; preds = %if_true, %while_cond
  %count8 = load i32, i32* %count, align 4
  ret i32 %count8

if_true:                                          ; preds = %while_body
  br label %while_end
  br label %next

if_false:                                         ; preds = %while_body
  br label %next

next:                                             ; preds = %if_false, %if_true
  br label %while_cond
}
