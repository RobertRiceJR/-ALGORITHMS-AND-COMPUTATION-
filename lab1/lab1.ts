function numTilings(n: number): number {
    const mod = 1000000007;
    // dp[i] to store the number of tilings for a 2 x i board.
    const dp: number[] = new Array(n + 1).fill(0);
    
    // base cases
    dp[0] = 1;  
    dp[1] = 1;
    if(n >= 2) {
        dp[2] = 2;
    }
    
    //table using the recurrence:
    for (let i = 3; i <= n; i++) {
        dp[i] = (2 * dp[i - 1] + dp[i - 3]) % mod;
    }
    
    return dp[n];
}
