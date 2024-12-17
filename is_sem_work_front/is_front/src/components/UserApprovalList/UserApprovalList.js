import React, { useEffect, useState } from 'react';
import './UserApprovalList.css'

const API_BASE_URL = 'http://localhost:8080/api/book-creatures';

const UserApprovalList = ({ token }) => {
    const [users, setUsers] = useState([]);

    const getUsersNeedingApproval = async (token) => {
        const response = await fetch(`${API_BASE_URL}/admin`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error('Failed to fetch users');
        }
        return await response.json();
    };


    const approveUser = async (username, token) => {
        const response = await fetch(`${API_BASE_URL}/users/approve`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                isApproved: true,
                isAdmin: false
            })
        });

        if (!response.ok) {
            throw new Error('Failed to approve user');
        }
        return await response.json();
    };



    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const usersData = await getUsersNeedingApproval(token);
                setUsers(usersData);
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();
    }, [token]);

    const handleApprove = async (username) => {
        try {
            await approveUser(username, token);
            setUsers(users.filter(user => user.username !== username));
        } catch (error) {
            console.error("Error approving user:", error);
        }
    };



    return (
        <div className="approval-container">
            <h2 className="title">Список пользователей желающих стать админами</h2>
            <ul className="user-list">
                {users.map(user => (
                    <li key={user.id} className="user-item">
                        <span className="username">{user.username}</span>
                        <label className="approve-label">
                            <input
                                type="checkbox"
                                className="approve-checkbox"
                                onChange={() => handleApprove(user.username)}
                                onClick={(e) => e.stopPropagation()}
                            />
                            Approve
                        </label>

                    </li>
                ))}
            </ul>
        </div>
    );

};

export default UserApprovalList;