"""Initial Migration of Farm-App

Revision ID: c9209dcaef18
Revises: ef2de754f079
Create Date: 2023-09-14 10:22:40.966987

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'c9209dcaef18'
down_revision: Union[str, None] = 'ef2de754f079'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    pass


def downgrade() -> None:
    pass
