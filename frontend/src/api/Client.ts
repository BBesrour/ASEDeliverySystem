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

    async postRequest(url: string, params: any = {}, body: any = {}) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const csrf_token = await fetch(this.urlBase + "/csrf", {
            method: "GET"
        }).then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json()
        })
        const response = await fetch(this.service + urlWithParams, {
            method: 'POST',
            headers: {
                'X-XSRF-TOKEN': csrf_token.token,
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        return await response.json();
    }

    async putRequest(url: string, params: any = {}, body: any = {}) {
        const urlWithParams = url + (Object.keys(params).length ? `?${new URLSearchParams(params)}` : '');
        const csrf_token = await fetch(this.urlBase + "/csrf", {
            method: "GET"
        }).then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json()
        })
        const response = await fetch(this.service + urlWithParams, {
            method: 'PUT',
            headers: {
                'X-XSRF-TOKEN': csrf_token.token,
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
        const csrf_token = await fetch(this.urlBase + "/csrf", {
            method: "GET"
        }).then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json()
        })
        const response = await fetch(this.service + urlWithParams, {
            method: 'DELETE',
            headers: {
                'X-XSRF-TOKEN': csrf_token.token,
                'Content-Type': 'application/json',
                // add authorization header with access token
                'Authorization': `Bearer ${getAccessToken()}`
            },
            body: body ? JSON.stringify(body) : undefined
        });
        return await response.json();
    }

}
