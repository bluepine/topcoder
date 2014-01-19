bool solve(vector<int> cubes, int B, int w, string view)
{
    int n = view.size();
    bool valid = true;
    int i;
    int tofill_evens = 0;
    int tofill_odds = 0;

    for (i = 0; i < n; i++) {
        int block = view[i] - '0';
        if (block >= 0 && block <= 2) {
            cubes[block]--;
            if (cubes[block] < 0) {
                valid = false;
                break;
            }
        } else { // "b"
            int j;
            for (j = i + 1; j < n && view[j] == 'b'; j++)
                ;
            if ((j - i) % 2 == 0) {
                B -= (j - i) / 2;
            } else{
                if (i == 0 && j == 1) {
                    valid = false;
                    break;
                }
                B -= (j - i + 1) / 2;
                w--;
                if (i >= 1) {
                    tofill_evens += (i - 1) / 2;
                    tofill_odds += (i - 1) % 2;
                } else
                    tofill_odds ++;
            }
            i = j - 1;
            if (B < 0 || w <= 0) {
                valid = false;
                break;
            }
        }
    }

    if (!valid) {
        return false;
    }

    // process tofill
    int remain = cubes[0] + cubes[1] + cubes[2];
    valid = remain >= tofill_odds && remain + B * 2 >= tofill_odds + tofill_evens * 2;
    return valid;
}
