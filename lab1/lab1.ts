function numTilings(n: number): number {
    const mod = 1000000007;
    // dp[i] will store the number of tilings for a 2 x i board.
    const dp: number[] = new Array(n + 1).fill(0);
    
    // Base cases:
    dp[0] = 1; 
    dp[1] = 1;
    if(n >= 2) {
        dp[2] = 2;
    }
    
    // Build the dp table using the recurrence:
    for (let i = 3; i <= n; i++) {
        dp[i] = (2 * dp[i - 1] + dp[i - 3]) % mod;
    }
    console.log(dp[n])
    return dp[n];
}

numTilings(5)
