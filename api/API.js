export class API {
    static baseUrl = "http://localhost:8080";

    static getAuthHeaders(token) {
        return token ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" } : { "Content-Type": "application/json" };
    }

    static async GET(url, token) {
        const res = await fetch(`${this.baseUrl}${url}`, {
            method: "GET",
            headers: this.getAuthHeaders(token)
        });

        if (res.status !== 200 && res.status !== 201) return { status: res.status }

        const data = await res.json();
        data.status = res.status;
        return data;
    }

    static async POST(url, body, token) {
        const res = await fetch(`${this.baseUrl}${url}`, {
            method: "POST",
            headers: this.getAuthHeaders(token),
            body: JSON.stringify(body)
        });

        if (res.status !== 200 && res.status !== 201) return { status: res.status }

        const data = await res.json();
        data.status = res.status;
        return data;
    }

    static async PATCH(url, body, token) {
        const res = await fetch(`${this.baseUrl}${url}`, {
            method: "PATCH",
            headers: this.getAuthHeaders(token),
            body: JSON.stringify(body)
        });

        if (res.status !== 200 && res.status !== 201) return { status: res.status }

        const data = await res.json();
        data.status = res.status;
        return data;
    }
}
