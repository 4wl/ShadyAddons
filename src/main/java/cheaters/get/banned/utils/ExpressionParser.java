/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.utils;

public class ExpressionParser {
    public static double eval(final String str) {
        return new Object(){
            int pos = -1;
            int ch;

            void nextChar() {
                this.ch = ++this.pos < str.length() ? (int)str.charAt(this.pos) : -1;
            }

            boolean eat(int charToEat) {
                while (this.ch == 32) {
                    this.nextChar();
                }
                if (this.ch == charToEat) {
                    this.nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                this.nextChar();
                double x = this.parseExpression();
                if (this.pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char)this.ch);
                }
                return x;
            }

            double parseExpression() {
                double x = this.parseTerm();
                while (true) {
                    if (this.eat(43)) {
                        x += this.parseTerm();
                        continue;
                    }
                    if (!this.eat(45)) break;
                    x -= this.parseTerm();
                }
                return x;
            }

            double parseTerm() {
                double x = this.parseFactor();
                while (true) {
                    if (this.eat(42)) {
                        x *= this.parseFactor();
                        continue;
                    }
                    if (!this.eat(47)) break;
                    x /= this.parseFactor();
                }
                return x;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            double parseFactor() {
                double x;
                if (this.eat(43)) {
                    return this.parseFactor();
                }
                if (this.eat(45)) {
                    return -this.parseFactor();
                }
                int startPos = this.pos;
                if (this.eat(40)) {
                    x = this.parseExpression();
                    this.eat(41);
                } else if (this.ch >= 48 && this.ch <= 57 || this.ch == 46) {
                    while (this.ch >= 48 && this.ch <= 57 || this.ch == 46) {
                        this.nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    if (this.ch < 97) throw new RuntimeException("Unexpected: " + (char)this.ch);
                    if (this.ch > 122) throw new RuntimeException("Unexpected: " + (char)this.ch);
                    while (this.ch >= 97 && this.ch <= 122) {
                        this.nextChar();
                    }
                    String func = str.substring(startPos, this.pos);
                    x = this.parseFactor();
                    if (func.equals("sqrt")) {
                        x = Math.sqrt(x);
                    } else if (func.equals("sin")) {
                        x = Math.sin(Math.toRadians(x));
                    } else if (func.equals("cos")) {
                        x = Math.cos(Math.toRadians(x));
                    } else {
                        if (!func.equals("tan")) throw new RuntimeException("Unknown function: " + func);
                        x = Math.tan(Math.toRadians(x));
                    }
                }
                if (!this.eat(94)) return x;
                return Math.pow(x, this.parseFactor());
            }
        }.parse();
    }
}

