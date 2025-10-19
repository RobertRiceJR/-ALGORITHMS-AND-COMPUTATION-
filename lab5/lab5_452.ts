function findMinArrowShots(points: number[][]): number {
    const n = points.length;
    if (n === 0) return 0;

    // Sort by End Position, Break Ties with Start Position
    points.sort((a, b) => (a[1] - b[1]) || (a[0] - b[0]));

    let arrows = 1;
    // shoot at the end of the first ballon
    let arrowX = points[0][1];

    for (let i = 1; i < n; i++) {
        const [start, end] = points[i];
        if (start > arrowX) {
            arrows++;
            arrowX = end;
        }
    }
    return arrows;
};