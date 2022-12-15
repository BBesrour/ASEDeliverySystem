export function getAccessToken(): string {
    return localStorage.getItem('accessToken') || '';
}

export function setAccessToken(accessToken: string) {
    localStorage.setItem('accessToken', accessToken);
}

export function removeAccessToken() {
    localStorage.removeItem('accessToken');
}


export function getRoles(): string[] {
    return JSON.parse(localStorage.getItem('roles') || '[]');
}

export function setRoles(roles: string[]) {
    localStorage.setItem('roles', JSON.stringify(roles));
}