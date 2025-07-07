from rest_framework.permissions import BasePermission
from rest_framework.exceptions import PermissionDenied 

class IsAdminUserFromToken(BasePermission):
    
    def has_permission(self, request, view):
        user = request.user

        if not hasattr(user, 'role'):
            return False 
        
        return user.role == 'ADMIN'