from django.shortcuts import render
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticatedOrReadOnly
from .models import Question, Option
from .serializers import QuestionListSerializer, QuestionSerializer, CorrectOptionSerializer
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

class AnswerFetchView(APIView):
    permission_classes = [IsAdminUserFromToken]

    def get(self, request, question_id):
        if not question_id:
            return Response({"error": "Question ID is required"}, status=status.HTTP_400_BAD_REQUEST)

        try:
            question = Question.objects.get(id=question_id)
        except Question.DoesNotExist:
            return Response({"error": "Question not found"}, status=status.HTTP_404_NOT_FOUND)

        try:
            correct_option = Option.objects.get(question=question, is_correct=True)
        except Option.DoesNotExist:
            return Response({"error": "Correct option not found"}, status=status.HTTP_404_NOT_FOUND)
        except Option.MultipleObjectsReturned:
            return Response({"error": "Multiple correct options found. Data inconsistency."}, status=500)

        data = {
            "correct_option_id": correct_option.id,
            "correct_option_text": correct_option.text
        }

        serializer = CorrectOptionSerializer(data)
        return Response(serializer.data)
    

class QuestionBatchFetchView(APIView):
    permission_classes = [IsAdminUserFromToken]

    def post(self, request):
        id_list = self.request.data.get('id_list', [])
        questions = Question.objects.filter(id__in=id_list)
        serializer = QuestionSerializer(questions, many=True)
        return Response(serializer.data)
    