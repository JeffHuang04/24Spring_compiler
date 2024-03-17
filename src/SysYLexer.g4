lexer grammar SysYLexer;

CONST : 'const';

INT : 'int';

VOID : 'void';

IF : 'if';

ELSE : 'else';

WHILE : 'while';

BREAK : 'break';

CONTINUE : 'continue';

RETURN : 'return';

PLUS : '+';

MINUS : '-';

MUL : '*';

DIV : '/';

MOD : '%';

ASSIGN : '=';

EQ : '==';

NEQ : '!=';

LT : '<';

GT : '>';

LE : '<=';

GE : '>=';

NOT : '!';

AND : '&&';

OR : '||';

L_PAREN : '(';

R_PAREN : ')';

L_BRACE : '{';

R_BRACE : '}';

L_BRACKT : '[';

R_BRACKT : ']';

COMMA : ',';

SEMICOLON : ';';

//以下划线或字母开头，仅包含下划线、英文字母大小写、阿拉伯数字
IDENT : ('_' | LETTER) WORD* ;

// 数字常量，包含十进制数，0开头的八进制数，0x或0X开头的十六进制数
INTEGER_CONST :   Decimal | Octal | Hexadecimal;

WS : [ \r\n\t]+ -> skip;

LINE_COMMENT : '//' .*? '\n' -> skip;//问号的作用是匹配最短的可能性->非贪婪匹配

MULTILINE_COMMENT : '/*' .*? '*/' -> skip;

fragment LETTER : [a-zA-Z];
fragment NUMBER : [0-9];
fragment WORD : '_' | LETTER | NUMBER;
fragment Decimal : '0' |[1-9]NUMBER*;//正负号单独处理
fragment Octal : '0' [0-7]+;
fragment Hexadecimal : ('0x' | '0X') [0-9a-fA-F]+;

