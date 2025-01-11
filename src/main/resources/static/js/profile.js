document.addEventListener('DOMContentLoaded', () => {
    const adminPanelLink = document.querySelector('.sidebar a[href="/admin"]');

    const checkUserRole = async () => {
        try {
            const response = await fetch('/api/current-user');
            if (!response.ok) {
                throw new Error('Error');
            }
            const user = await response.json();
            console.log('Current user:', user);

            const isAdmin = user.roles.some(role => role.name === 'ROLE_ADMIN');
            console.log('Admin:', isAdmin);

            if (isAdmin) {
                adminPanelLink.style.display = 'block';
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    checkUserRole();
});