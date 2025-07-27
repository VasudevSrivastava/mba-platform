from django.urls import path
from .views import QuestionDetailView, QuestionBatchFetchView, QuestionListView, AnswerFetchView

urlpatterns = [
    path("", QuestionListView.as_view(), name="question-list"),
    path("batch/", QuestionBatchFetchView.as_view(), name="question-batch"),
    path("<uuid:pk>/", QuestionDetailView.as_view(), name="question-detail"),
    path("<uuid:question_id>/answer", AnswerFetchView.as_view(), name="answer-fetch"),
]