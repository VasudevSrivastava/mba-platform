from django.urls import path
from .views import QuestionDetailView, QuestionBatchFetchView, QuestionListView

urlpatterns = [
    path("", QuestionListView.as_view(), name="question-list"),
    path("batch/", QuestionBatchFetchView.as_view(), name="question-batch"),
    path("<uuid:pk>/", QuestionDetailView.as_view(), name="question-detail")
]