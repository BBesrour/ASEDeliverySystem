import { getAccessToken } from "../storage/user";

export default class Client {
    private readonly service: string;
    private readonly urlBase: string;

    constructor(urlBase: string, service: string) {
        this.service = urlBase + service;
        this.urlBase = urlBase;
    }

    async request(method: string, url: string, params: any, body: any) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const response = await fetch(this.service + urlWithParams, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        if (!response.ok) {
            const error = new Error("Failed Request");
            // @ts-ignore
            error.response = response;

            throw error;
        }
        return await response.json();
    }

    getRequest(url: string, params: any = {}) {
        return this.request('GET', url, params, null);
    }

    async getCSRFToken() {
        const csrfToken = await fetch(this.urlBase + "/csrf", {
            method: "GET"
        }).then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json()
        });
        return csrfToken.token
    }

    responseCheck(response: Response) {
        if (!response.ok) {
            const error = new Error("Failed Request");
            // @ts-ignore
            error.response = response;

            throw error;
        }
    }

    async postRequest(url: string, params: any = {}, body: any = {}) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const csrfToken = await this.getCSRFToken();
        const response = await fetch(this.service + urlWithParams, {
            method: 'POST',
            headers: {
                'X-XSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        this.responseCheck(response);
        return await response.json();
    }

    async putRequest(url: string, params: any = {}, body: any = {}) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const csrfToken = await this.getCSRFToken();
        const response = await fetch(this.service + urlWithParams, {
            method: 'PUT',
            headers: {
                'X-XSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        return await response.json();
    }

    async deleteRequest(url: string, params: any = {}, body: any = {}) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const csrfToken = await this.getCSRFToken();
        const response = await fetch(this.service + urlWithParams, {
            method: 'DELETE',
            headers: {
                'X-XSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        return await response.json();
    }

}
