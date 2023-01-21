export default class Client {
    private readonly urlBase: string

    constructor(urlBase: string) {
        this.urlBase = urlBase;
    }

    async request(method: string, url: string, params: any, body: any) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const response = await fetch(this.urlBase + urlWithParams, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: body ? JSON.stringify(body) : undefined
        });
        return await response.json();
    }

    getRequest(url: string, params: any = {}) {
        return this.request('GET', url, params, null);
    }

    postRequest(url: string, params: any = {}, body: any = {}) {
        return this.request('POST', url, params, body);
    }

    putRequest(url: string, params: any = {}, body: any = {}) {
        return this.request('PUT', url, params, body);
    }

    deleteRequest(url: string, params: any = {}, body: any = {}) {
        return this.request('DELETE', url, params, body);
    }
}