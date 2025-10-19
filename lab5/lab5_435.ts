function eraseOverlapIntervals(intervals: number[][]): number {
    const n =  intervals.length;
    if( n <= 1) return 0;

    //Sort by End Time, Break Ties with Start Time
    intervals.sort((a, b) => (a[1] - b[1]) || (a[0] - b[0]));

    let count = 0;
    let lastEnd = Number.NEGATIVE_INFINITY;
    
    for(const [start, end] of intervals) {
        if(start >= lastEnd) {
            count++;
            lastEnd = end;
        }
    }

    return n - count;
}