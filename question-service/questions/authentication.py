from rest_framework.authentication import BaseAuthentication
from rest_framework.exceptions import AuthenticationFailed
from django.contrib.auth.models import AnonymousUser
import jwt

class CustomJWTAuthentication(BaseAuthentication):
    
    def authenticate(self, request):
        auth_header = request.headers.get('Authorization')

        if not auth_header or not auth_header.startswith('Bearer '):
            return None
        
        token = auth_header.split(' ')[1]

        try:
            decoded = jwt.decode(
                token,
                "whats my name? what's by name, what's my name? my name is sheelaaa",
                algorithms=['HS256']
            )
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed("Token Expired")
        except jwt.InvalidTokenError:
            raise AuthenticationFailed("Invalid Token")
        
        class User:
            pass
        user = User()
        user.id = decoded.get('sub')
        user.role = decoded.get('role')

        return (user, token)