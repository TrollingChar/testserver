import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Base64Codec {
    static char[] chArray;
    static Map<Character, Integer> chMap;
    static boolean initialized = false;

    static public void Init() {
        Init("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./");
    }

    static public void Init(String s) {
        chArray = s.toCharArray();
        chMap = new HashMap<>();
        for(int i=0; i<chArray.length; i++){
            chMap.put(chArray[i], i);
        }
        initialized = true;
    }

    static public char EncodeToChar(int n) {
        if(!initialized) Init();
        return (n & ~63) == 0 ? chArray[n] : '?';
    }

    static public String Encode(int n) {
        if(!initialized) Init();
        String s = "=";
        while (n != 0) {
            s = EncodeToChar(n & 63) + s;
            n >>= 6;
        }
        return s;
    }

    static public int Decode(char c) {
        if(!initialized) Init();
        return chMap.containsKey(c) ? chMap.get(c) : -1;
    }

    static public int DecodeFromString(WrapperString value)
    {
        if(!initialized) Init();
        char[] charArray = value.s.toCharArray();
        int sum = 0;
        for(int i = 0; i < value.s.length(); i++)
        {
            if(charArray[i] != '=')
            {
                sum = sum * 64 + Decode(charArray[i]);
            }
            else
            {
                //sum = sum * 64 + charArray[i];
                value.s = value.s.substring(++i);
                return sum;
            }
        }
        return -1;
    }
}
