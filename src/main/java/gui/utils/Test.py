import sys

def test(str1, str2):
    print(f"str1: {str1}, str2: {str2}")


if __name__ == "__main__":
    str1 = sys.argv[1]
    str2 = sys.argv[2]
    test(str1, str2)
