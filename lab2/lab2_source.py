class Solution:
    def multiply(self, num1: str, num2: str) -> str:
      
        def strip_leading_zeros(s: str) -> str:
            i = 0
            while i < len(s) - 1 and s[i] == '0':
                i += 1
            return s[i:]

        def add_strings(a: str, b: str) -> str:
            i, j = len(a) - 1, len(b) - 1
            carry = 0
            res = []

            while i >= 0 or j >= 0 or carry:
                d1 = ord(a[i]) - 48 if i >= 0 else 0
                d2 = ord(b[j]) - 48 if j >= 0 else 0
                s = d1 + d2 + carry
                res.append(chr(s % 10 + 48))
                carry = s // 10
                i -= 1
                j -= 1

            return ''.join(reversed(res))

        def compare_strings(a: str, b: str) -> int:
            a = strip_leading_zeros(a)
            b = strip_leading_zeros(b)
            if len(a) < len(b):
                return -1
            if len(a) > len(b):
                return 1
            if a < b:
                return -1
            if a > b:
                return 1
            return 0

        def subtract_strings(a: str, b: str) -> str:
            # normalize and assert a >= b
            if compare_strings(a, b) == 0:
                return "0"
            assert compare_strings(a, b) >= 0

            i, j = len(a) - 1, len(b) - 1
            borrow = 0
            res = []

            while i >= 0:
                d1 = ord(a[i]) - 48 - borrow
                d2 = ord(b[j]) - 48 if j >= 0 else 0

                if d1 < d2:
                    d1 += 10
                    borrow = 1
                else:
                    borrow = 0

                res.append(chr(d1 - d2 + 48))
                i -= 1
                j -= 1

            s = ''.join(reversed(res))
            return strip_leading_zeros(s)

        def grade_school_multiply(x: str, y: str) -> str:
            x = strip_leading_zeros(x)
            y = strip_leading_zeros(y)
            if x == "0" or y == "0":
                return "0"

            m, n = len(x), len(y)
            res = [0] * (m + n)

            for i in range(m - 1, -1, -1):
                n1 = ord(x[i]) - 48
                carry = 0
                for j in range(n - 1, -1, -1):
                    n2 = ord(y[j]) - 48
                    s = res[i + j + 1] + n1 * n2 + carry
                    res[i + j + 1] = s % 10
                    carry = s // 10
                res[i] += carry

            i = 0
            while i < len(res) - 1 and res[i] == 0:
                i += 1
            return ''.join(str(d) for d in res[i:])

        THRESHOLD = 32 

        def karatsuba(x: str, y: str) -> str:
            x = strip_leading_zeros(x)
            y = strip_leading_zeros(y)

            if x == "0" or y == "0":
                return "0"

            n = max(len(x), len(y))
            if n <= THRESHOLD:
                return grade_school_multiply(x, y)

            x = x.zfill(n)
            y = y.zfill(n)
            if n % 2 == 1:
                n += 1
                x = x.zfill(n)
                y = y.zfill(n)

            m = n // 2
            a, b = x[:m], x[m:]
            c, d = y[:m], y[m:]

            ac = karatsuba(a, c)
            bd = karatsuba(b, d)
            ab = add_strings(a, b)
            cd = add_strings(c, d)
            abcd = karatsuba(ab, cd)

            t = subtract_strings(abcd, ac)
            ad_plus_bc = subtract_strings(t, bd)

            ac_shift = ac + "0" * (2 * m)
            ad_shift = ad_plus_bc + "0" * m

            result = add_strings(add_strings(ac_shift, ad_shift), bd)
            return strip_leading_zeros(result)

        if num1 == "0" or num2 == "0":
            return "0"
        return karatsuba(num1, num2)
