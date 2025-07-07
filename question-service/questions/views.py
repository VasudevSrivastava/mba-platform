from django.shortcuts import render
from rest_framework import generics
from rest_framework.permissions import IsAuthenticatedOrReadOnly
from .models import Question
from .serializers import QuestionListSerializer, QuestionSerializer
from rest_framework.views import APIView
from rest_framework.response import Response
from .permissions import IsAdminUserFromToken

class QuestionListView(generics.ListAPIView):
    queryset = Question.objects.all()
    serializer_class = QuestionListSerializer
    permission_classes = [IsAuthenticatedOrReadOnly]

class QuestionDetailView(generics.RetrieveAPIView):
    queryset = Question.objects.all()
    serializer_class = QuestionSerializer
    permission_classes = [IsAuthenticatedOrReadOnly]

class QuestionBatchFetchView(APIView):
    permission_classes = [IsAdminUserFromToken]

    def post(self, request):
        id_list = self.request.data.get('id_list', [])
        questions = Question.objects.filter(id__in=id_list)
        serializer = QuestionSerializer(questions, many=True)
        return Response(serializer.data)
    