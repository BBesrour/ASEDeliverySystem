export function getAccessToken(): string {
    return localStorage.getItem('accessToken') || '';
}

export function setAccessToken(accessToken: string) {
    localStorage.setItem('accessToken', accessToken);
}

export function removeAccessToken() {
    localStorage.removeItem('accessToken');
}


export function getRole(): string {
    return JSON.parse(localStorage.getItem('role') || '[]');
}

export function setRole(role: string) {
    localStorage.setItem('role', JSON.stringify(role));
}

export function getUserID(): string {
    return localStorage.getItem('userID') || '';
}

export function setUserID(userID: string) {
    localStorage.setItem('userID', userID);
}