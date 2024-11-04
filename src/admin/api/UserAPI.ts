import api from "../../axious/axious";
import User from "../../models/User";

interface ResultInterface {
    result: User[];
    pageTotal: number;
    totalElements: number;
}

export async function getAllUsers(page: number, pageSize: number): Promise<ResultInterface | null> {
    const result: User[] = [];

    try {
        const response = await api.get(`/users?page=${page}&size=${pageSize}`);
        if (response.data.code === 1000) {
            const responseData = response.data.result.data;
            result.push(...responseData.map((user: any) => ({
                id: user.id,
                userName: user.userName,
                fullName: user.fullName,
                email: user.email,
                phoneNumber: user.phoneNumber,
                roles: user.roles.map((role: any) => ({
                    id: role.id,
                    name: role.name
                }))
            })));

            const pageTotal: number = response.data.result.totalPages || 1;
            const totalElements: number = response.data.result.totalElements || result.length;

            return {
                result: result,
                pageTotal: pageTotal,
                totalElements: totalElements
            };
        } else {
            console.error("Error in response: ", response.data.message);
            return null;
        }
    } catch (error) {
        console.error("Error fetching users: ", error);
        return null;
    }
}

export async function findByUserName(name: string, page: number, pageSize: number): Promise<ResultInterface | null> {
    const endpoint: string = `/users/search-name?name=${encodeURIComponent(name)}&page=${page}&size=${pageSize}`;
    const result: User[] = [];

    try {
        const response = await api.get(endpoint);

        if (response.data.code === 1000) {
            const responseData = response.data.result.data;
            result.push(...responseData.map((user: any) => ({
                id: user.id,
                userName: user.userName,
                fullName: user.fullName,
                email: user.email,
                phoneNumber: user.phoneNumber,
                roles: user.roles.map((role: any) => ({
                    id: role.id,
                    name: role.name
                }))
            })));

            const pageTotal: number = response.data.result.totalPages || 1;
            const totalElements: number = response.data.result.totalElements || result.length;

            return {
                result: result,
                pageTotal: pageTotal,
                totalElements: totalElements
            };
        } else {
            console.error("Error in response: ", response.data.message);
            return null;
        }
    } catch (error) {
        console.error("Error fetching users by name: ", error);
        return null;
    }
}